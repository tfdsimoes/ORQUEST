import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

import { EmployeeModel } from '../models/employee.model';
import { ApiService } from './api.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private currentEmployeeSubject: BehaviorSubject<EmployeeModel>;
  public currentEmployee: Observable<EmployeeModel>;

  constructor(
    private apiService: ApiService
  ) {
    this.currentEmployeeSubject = new BehaviorSubject<EmployeeModel>(JSON.parse(localStorage.getItem('currentUser')));
    this.currentEmployee = this.currentEmployeeSubject.asObservable();
  }

  public get currentEmployeeValue(): EmployeeModel {
    return this.currentEmployeeSubject.value;
  }

  login(employeeNumber) {
    return this.apiService.post('/employee/login', {number: employeeNumber})
      .pipe(map(employee => {
        localStorage.setItem('currentEmployee', JSON.stringify(employee));
        this.currentEmployeeSubject.next(employee);
        return employee;
      }));
  }

  logout() {
    localStorage.removeItem('employee');
    this.currentEmployeeSubject.next(null);
  }
}
