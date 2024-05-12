import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { IUser } from 'src/app/models/user';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-add-trip',
  templateUrl: './add-trip.component.html',
  styleUrls: ['./add-trip.component.css']
})
export class AddTripComponent implements OnInit {
  baseUrl = environment.apiUrl;
  tripForm!:FormGroup;
  users: IUser[] = [];

  constructor(private http:HttpClient,
    private dialogRef: MatDialogRef<AddTripComponent>
) { }

  ngOnInit(): void {
    this.http.get<IUser>(this.baseUrl +'users/all').subscribe((users: any) => {
      this.users = users;
    });
    this.tripForm = new FormGroup({
      tripName: new FormControl('', Validators.required),
      location: new FormControl('', Validators.required),
      admin: new FormControl(null, Validators.required),
      groupSize: new FormControl(1, [Validators.required,Validators.min(1)])   
    });
  }

  onSubmit(): void {
    if (this.tripForm.valid) {
      const formData = this.tripForm.value;
      const url = `${this.baseUrl}trips/create`;
      const adminValue = formData.admin;
      formData.admin = this.users.find(user =>user.id == adminValue);

      this.http.post(url, formData).subscribe((response) => {
        console.log('Trip deleted successfully:', response);
      },
      (error) => {
        console.error('Error deleting trip:', error);
      }
      );
    } else {
      this.tripForm.markAllAsTouched();
    }
  }

}
