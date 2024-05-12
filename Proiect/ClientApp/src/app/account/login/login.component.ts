import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { IUser } from 'src/app/models/user';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  baseUrl = environment.apiUrl;
  error = false;
  constructor(private http: HttpClient,
     private router: Router, private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.createLoginForm();

  }

  createLoginForm(){
    this.loginForm = new FormGroup({
      email: new FormControl('', [Validators.required, Validators.pattern('^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$')]),
      password: new FormControl('', Validators.required)
    });
  }

  onSubmit(){
    this.http.post<IUser>(this.baseUrl + 'users/login', this.loginForm.value).subscribe(
      (response) => {
        this.router.navigateByUrl('/');
      },
      (error) => {
        this.error = true;
        console.error('Error occurred during login:', error);
      }
    );
    }
  }