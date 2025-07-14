import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  async getData(): Promise<any> {
    // Remplace l’URL par celle de ton API
    return firstValueFrom(this.http.get('https://jsonplaceholder.typicode.com/users'));
  }
}
