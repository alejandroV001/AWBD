import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IUser } from 'src/app/models/user';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-add-users',
  templateUrl: './add-users.component.html',
  styleUrls: ['./add-users.component.css']
})
export class AddUsersComponent implements OnInit {
  baseUrl = environment.apiUrl;
  tripForm!:FormGroup;
  users: IUser[] = [];
  users2: IUser[] = [];

  currentUsers: IUser[] = [];
  currentUsersIds: string[] = [];

  constructor(private http:HttpClient,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) { }

  ngOnInit(): void {
    this.http.get<IUser>(`${this.baseUrl}trips/getMembers?tripId=${this.data.trip.id}`).subscribe((users: any) => {
      this.currentUsersIds = users;
      this.http.get<IUser>(this.baseUrl +'users/all').subscribe((users: any) => {
        this.users = users;
        this.filter();
      });
    });
    
    
    
    this.tripForm = new FormGroup({
      tripName: new FormControl('', Validators.required),
      location: new FormControl('', Validators.required),
      admin: new FormControl(null, Validators.required),
      groupSize: new FormControl(1, [Validators.required,Validators.min(1)])   
    });
  }

  private filter(){
    this.users.forEach(element => {
      this.currentUsersIds.forEach(id => {
        console.log(element.id.toString(),id)
        if(element.id.toString() == id) {
          console.log(element);
          this.currentUsers.push(element);
        } 
      });
      
    });
    console.log(this.currentUsers);
  }
  addUser(user: IUser){
    const tripId = this.data.trip.id;
    const url = `${this.baseUrl}trips/addMember?tripId=${tripId}&userId=${user.id}`;

    const formData = {
      tripId: tripId,
      userId: user.id 
    };
    this.http.post(url, formData).subscribe((response) => {
      console.log('Trip deleted successfully:', response);
    },
    (error) => {
      console.error('Error deleting trip:', error);
    }
    );
  }

  removeUser(user: IUser){
    const tripId = this.data.trip.id;
    const url = `${this.baseUrl}trips/removeMember?tripId=${tripId}&userId=${user.id}`;
    var index = this.currentUsers.findIndex(user => user === user);

    this.http.delete(url).subscribe((response) => {
      console.log('Trip deleted successfully:', response);
    },
    (error) => {
      console.error('Error deleting trip:', error);
    }
    );
  }
}
