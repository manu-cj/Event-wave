import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {firstValueFrom, Observable} from 'rxjs';
import {IReservation, IReservationPage} from '../models/reservation.model';
import {environment} from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // Fetch reservations with pagination and authorization token
  getReservations(param: string,token: string, page: number, size: number, column: string, direction: string): Observable<IReservationPage> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>(`${this.baseUrl}/reservation?param=${param}&page=${page}&size=${size}&sort=${column},${direction}`, { headers });
  }

  // Post a new reservation and return the created reservation
  async postReservation(reservation: IReservation): Promise<IReservation> {
    console.log(reservation);
    return firstValueFrom(this.http.post<IReservation>(`${this.baseUrl}/reservation`, reservation));
  }

  // Get reservations for the current user using an authorization token
  getUserReservations(token: string,  page: number, size: number): Observable<IReservationPage> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>(`${this.baseUrl}/reservation/user?page=${page}&size=${size}`, { headers });
  }

  downloadTicket(reservationId: string): Observable<Blob> {
    return this.http.get(`/api/reservation/${reservationId}/ticket`, { responseType: 'blob' });
  }
}
