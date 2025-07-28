import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {firstValueFrom, Observable} from 'rxjs';
import {ILogin, IRegister, IUser, IUserPage} from '../models/user.model';
import {IReservation, IReservationPage} from '../models/reservation.model';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private baseUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  getReservations(token: string, page: number, size: number): Observable<IReservationPage> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>(`${this.baseUrl}/reservation?page=${page}&size=${size}`, { headers });
  }

  async postReservation(reservation: IReservation): Promise<IReservation> {
    return firstValueFrom(this.http.post<IReservation>(`${this.baseUrl}/reservation`, reservation));
  }

  getUserReservations(token: string): Observable<IReservationPage> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>(this.baseUrl + '/reservation/user', { headers });
  }





}
