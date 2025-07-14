import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  async getData(path?: string): Promise<any> {
    // Remplace l’URL par celle de ton API
    const baseUrl = 'https://jsonplaceholder.typicode.com/users';
    const url = path ? `${baseUrl}/${path}` : baseUrl;
    return firstValueFrom(this.http.get(url));
  }
}
