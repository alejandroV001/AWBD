import { HttpClient } from '@angular/common/http';
import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { IUser } from 'src/app/models/user';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-add-expense',
  templateUrl: './add-expense.component.html',
  styleUrls: ['./add-expense.component.css']
})
export class AddExpenseComponent implements OnInit {
  baseUrl = environment.apiUrl;
  expenseForm!:FormGroup;
  users: IUser[] = [];

  constructor(private http:HttpClient,@Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit(): void {
    this.expenseForm = new FormGroup({
      name: new FormControl('', Validators.required),
      currency: new FormControl('', Validators.required),
      amount: new FormControl(1, [Validators.required,Validators.min(1)])   
    });
    this.http.get<IUser>(this.baseUrl +'users/all').subscribe((users: any) => {
      this.users = users;
    });
  }

  onSubmit(): void {
    if (this.expenseForm.valid) {
      //const formData = this.expenseForm.value;
      const url = `${this.baseUrl}trip-expenses/addExpense`;

      const formData = {
        expense:{
          name: this.expenseForm.value.name,
          currency: this.expenseForm.value.currency,
          amount: this.expenseForm.value.name,
          trip: this.data.trip
        },
        trip: this.data.trip,
        user: this.users.find(user => user.id == 2)
      };
console.log(formData);
      this.http.post(url, formData).subscribe((response) => {
        console.log('Trip deleted successfully:', response);
      },
      (error) => {
        console.error('Error deleting trip:', error);
      }
      );
    } 
  }
}
