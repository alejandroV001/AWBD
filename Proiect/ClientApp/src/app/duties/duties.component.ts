import { Component } from '@angular/core';

@Component({
  selector: 'app-duties',
  templateUrl: './duties.component.html'
})
export class DutiesComponent {
  public currentCount = 0;

  public incrementCounter() {
    this.currentCount++;
  }
}
