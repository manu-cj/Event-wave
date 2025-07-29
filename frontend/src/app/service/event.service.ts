import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {IEvent, IEventPage} from '../models/event.model';
import {firstValueFrom, Observable} from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class EventService {
  private baseUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  async postEvent(event: IEvent, token: string, file?: File): Promise<IEvent> {
    const formData = new FormData();
    formData.append('event', new Blob([JSON.stringify(event)], { type: 'application/json' }));
    if (file) {
      formData.append('file', file);
    }
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return firstValueFrom(this.http.post<IEvent>(`${this.baseUrl}/events`, formData, { headers }));
  }

  getEvents(page:number, size:number): Observable<IEventPage> {
    return this.http.get<IEventPage>(`${this.baseUrl}/events?page=${page}&size=${size}`);
  }

  getEventById(id: string): Observable<IEvent> {
    return this.http.get<IEvent>(`${this.baseUrl}/events/${id}`);
  }

}

