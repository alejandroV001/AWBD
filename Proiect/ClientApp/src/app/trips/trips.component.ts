import { Component, OnInit } from '@angular/core';
import { Trip } from '../models/trip';
import { MatDialog } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { AddTripComponent } from './add-trip/add-trip/add-trip.component';
import { UpdateUsersComponent } from './add-trip/update-users/update-users.component';
import { AddUsersComponent } from './add-trip/users/add-users/add-users.component';
import { AddExpenseComponent } from './add-trip/add-expense/add-expense.component';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html'
})
export class TripsComponent implements OnInit {
  trips: Trip[] = [];
  displayedColumns: string[] = ['id','location', 'name','admin', 'options'];
  baseUrl = environment.apiUrl;

  constructor(private http: HttpClient,
    private dialog: MatDialog) { }

  ngOnInit(): void {
    this.loadAllTrips();
  }

  loadAllTrips(): void {
    this.http.get<Trip[]>(this.baseUrl + 'trips/all').subscribe(
      (response) => {
        console.log(response);
        this.trips = [...response];
      },
      (error) => {
        console.error('Error occurred during login:', error);
      }
    );
  }

  openUpdateDialog(trip: Trip): void {
    const dialogRef = this.dialog.open(UpdateUsersComponent, {
      width: '400px',
      data: { trip}
    });
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(AddTripComponent, {
      width: '400px',
    });

  }

  deleteTrip(tripId: number): void {
    const url = `${this.baseUrl}trips/${tripId}`; 
      this.http.delete(url).subscribe(
        (response) => {
          console.log('Trip deleted successfully:', response);
          this.loadAllTrips();
        },
        (error) => {
          console.error('Error deleting trip:', error);
        }
    );
  }

  addUser(trip: Trip): void {
    const dialogRef = this.dialog.open(AddUsersComponent, {
      width: '400px',
      data: { trip}
    });
  }

  addExpense(trip: Trip): void {
    const dialogRef = this.dialog.open(AddExpenseComponent, {
      width: '400px',
      data: { trip}
    });
  }
}
