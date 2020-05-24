import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { catchError } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(
    private http: HttpClient
  ) {
  }

  private formatErrors(error: any) {
    return throwError(error.error);
  }

  private buildHeader(params: HttpParams) {
    return {params, headers: new HttpHeaders().set('Content-Type', 'application/json')};
  }

  get(path: string, params: HttpParams = new HttpParams()): Observable<any> {
    const algo = {params, headers: new HttpHeaders().set('Content-Type', 'application/json')};

    return this.http.get(`${environment.api_url}${path}`, this.buildHeader(params))
      .pipe(catchError(this.formatErrors));
  }

  put(path: string, body: any = {}): Observable<any> {
    return this.http.put(`${environment.api_url}${path}`, JSON.stringify(body), this.buildHeader(null))
      .pipe(catchError(this.formatErrors));
  }

  post(path: string, body: any = {}): Observable<any> {
    return this.http.post(`${environment.api_url}${path}`, JSON.stringify(body), this.buildHeader(null))
      .pipe(catchError(this.formatErrors));
  }

  delete(path: string): Observable<any> {
    return this.http.delete(`${environment.api_url}${path}`, this.buildHeader(null))
      .pipe(catchError(this.formatErrors));
  }
}
