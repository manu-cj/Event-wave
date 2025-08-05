import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {IEvent, IEventPage} from '../models/event.model';
import {firstValueFrom, Observable} from 'rxjs';
import {environment} from '../../environment/environment';


@Injectable({
  providedIn: 'root'
})
export class EventService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  // Posts a new event, optionally with a file, and returns the created event
  async postEvent(event: IEvent, token: string, file?: File): Promise<IEvent> {
    const formData = new FormData();
    // Add event data as a JSON blob
    formData.append('event', new Blob([JSON.stringify(event)], { type: 'application/json' }));
    if (file) {
      // Add file if provided
      formData.append('file', file);
    }
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    // Send POST request to create event
    return firstValueFrom(this.http.post<IEvent>(`${this.baseUrl}/events`, formData, { headers }));
  }

  // Retrieves a paginated list of events
  getEvents(title: string ,page: number, size: number, sortColumn: string, sortDirection: string): Observable<IEventPage> {
    return this.http.get<IEventPage>(`${this.baseUrl}/events?title=${title}&page=${page}&size=${size}&sort=${sortColumn},${sortDirection}`);
  }

  // Retrieves a single event by its ID
  getEventById(id: string): Observable<IEvent> {
    return this.http.get<IEvent>(`${this.baseUrl}/events/${id}`);
  }

  getLastEvents(): Observable<IEvent[]> {
    return this.http.get<IEvent[]>(`${this.baseUrl}/events/lastEvent`);
  }
}

