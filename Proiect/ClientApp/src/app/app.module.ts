import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import { AppComponent } from './app.component';
import { NavMenuComponent } from './nav-menu/nav-menu.component';
import { HomeComponent } from './home/home.component';
import { TripsComponent } from './trips/trips.component';
import { LoginComponent } from './account/login/login.component';
import { RegisterComponent } from './account/register/register.component';
import { DutiesComponent } from './duties/duties.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatDialogModule } from '@angular/material/dialog';
import { MatTableModule } from '@angular/material/table';
import { MatListModule } from '@angular/material/list';
import { MatIcon } from '@angular/material/icon';

import { AddTripComponent } from './trips/add-trip/add-trip/add-trip.component';
import { UpdateUsersComponent } from './trips/add-trip/update-users/update-users.component';
import { AddUsersComponent } from './trips/add-trip/users/add-users/add-users.component';
import { RemoveUsersComponent } from './trips/add-trip/users/remove-users/remove-users.component';
import { AddExpenseComponent } from './trips/add-trip/add-expense/add-expense.component';

@NgModule({
  declarations: [
    AppComponent,
    NavMenuComponent,
    HomeComponent,
    TripsComponent,
    LoginComponent,
    RegisterComponent,
    DutiesComponent,
    AddTripComponent,
    UpdateUsersComponent,
    AddUsersComponent,
    RemoveUsersComponent,
    AddExpenseComponent
  ],
  imports: [
    BrowserModule.withServerTransition({ appId: 'ng-cli-universal' }),
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatDialogModule,
    MatTableModule,
    MatListModule,
    RouterModule.forRoot([
      { path: '', component: HomeComponent, pathMatch: 'full' },
      { path: 'trips', component: TripsComponent },
      { path: 'duties', component: DutiesComponent },
      { path: 'login', component: LoginComponent },
      { path: 'register', component: RegisterComponent }
    ]),
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
