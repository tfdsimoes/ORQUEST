import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../../core/services/authentication.service';
import { ApiService } from '../../core/services/api.service';
import { first } from 'rxjs/operators';
import { AlertModel } from '../../core/models/alert.model';
import { DayRegisterModel } from '../../core/models/day-register.model';
import { WeekRegisterModel } from '../../core/models/week-register.model';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {

  private employeeId: string;

  alerts: AlertModel[] = null;
  day: string;
  dayRegisters: DayRegisterModel = null;
  year: string;
  week: string;
  weekRegisters: WeekRegisterModel = null;

  constructor(
    private authenticationService: AuthenticationService,
    private apiService: ApiService
  ) {
  }

  ngOnInit() {
    this.employeeId = this.authenticationService.currentEmployeeValue.id;
  }

  loadWeekRegisters() {
    this.restartLists();

    this.apiService.get('/employee/' + this.employeeId + '/register/year/' + this.year + '/week/' + this.week)
      .pipe(first())
      .subscribe(
        (weekRegisters: WeekRegisterModel) => {
          this.weekRegisters = weekRegisters;
        }
      );
  }

  loadDayRegisters() {
    this.restartLists();

    this.apiService.get('/employee/' + this.employeeId + '/register/day/' + this.day)
      .pipe(first())
      .subscribe(
        (dayRegisters: DayRegisterModel) => {
          this.dayRegisters = dayRegisters;
        }
      );
  }

  loadAlerts() {
    this.restartLists();

    this.apiService.get('/employee/' + this.employeeId + '/alerts')
      .pipe(first())
      .subscribe(
        (alerts: AlertModel[]) => {
          this.alerts = alerts;
        }
      );
  }

  getFormatHours(input) {
    let totalHours;
    let totalMinutes;
    let totalSeconds;
    let hours;
    let minutes;
    let seconds;
    let result = '';

    totalSeconds = input / 1000;
    totalMinutes = totalSeconds / 60;
    totalHours = totalMinutes / 60;

    seconds = Math.floor(totalSeconds) % 60;
    minutes = Math.floor(totalMinutes) % 60;
    hours = Math.floor(totalHours) % 60;

    if (hours !== 0) {
      result += hours + ':';

      if (minutes.toString().length === 1) {
        minutes = '0' + minutes;
      }
    }

    result += minutes + ':';

    if (seconds.toString().length === 1) {
      seconds = '0' + seconds;
    }

    result += seconds;

    return result;
  }

  private restartLists() {
    this.alerts = null;
    this.dayRegisters = null;
    this.weekRegisters = null;
  }
}
