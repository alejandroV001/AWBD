import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IUser } from 'src/app/models/user';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-update-users',
  templateUrl: './update-users.component.html',
  styleUrls: ['./update-users.component.css']
})
export class UpdateUsersComponent implements OnInit {
  baseUrl = environment.apiUrl;
  tripForm!:FormGroup;
  users: IUser[] = [];
  
  constructor(private http:HttpClient,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.http.get<IUser>(this.baseUrl +'users/all').subscribe((users: any) => {
      this.users = users;
    });
    this.tripForm = new FormGroup({
      tripName: new FormControl(this.data.trip.tripName, Validators.required),
      location: new FormControl(this.data.trip.location, Validators.required),
      admin: new FormControl(this.data.trip.admin.id, Validators.required),
      groupSize: new FormControl(1, [Validators.required,Validators.min(1)])   
    });
  }

  onSubmit(): void {
    if (this.tripForm.valid) {
      const formData = this.tripForm.value;
      const url = `${this.baseUrl}trips/${this.data.trip.id}`;

      const adminValue = formData.admin;
      formData.admin = this.users.find(user =>user.id == adminValue);
      console.log(formData);
      this.http.put(url, formData).subscribe((response) => {
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
