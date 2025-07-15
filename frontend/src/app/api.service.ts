import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

import { User } from './models/user.model';
import { Product } from './models/product.model';

@Injectable({ providedIn: 'root' })
export class ApiService {
  // Remplace l’URL par celle de ton API
  private baseUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  async getData(path?: string): Promise<any> {
    const url = path ? `${this.baseUrl}/${path}` : this.baseUrl;
    return firstValueFrom(this.http.get(url));
  }

  // Api calls for a user
  async postUser(user: User): Promise<User> {
    return firstValueFrom(this.http.post<User>(`${this.baseUrl}/auth/register`, user));
  }

  async login(user: User): Promise<User> {
    return firstValueFrom(this.http.post<User>(`${this.baseUrl}/auth/login`, user));
  }

  async postAdmin(user: User): Promise<User> {
    return firstValueFrom(this.http.post<User>(`${this.baseUrl}/auth/register/admin`, user));
  }

  async getUserInfo(token: string): Promise<String> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return firstValueFrom(this.http.get<String>(`${this.baseUrl}/auth/me`, { headers }));
  }

  // Api calls for a product
  async postProduct(product: Product, token: string): Promise<Product> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return firstValueFrom(this.http.post<Product>(`${this.baseUrl}/products`, product, { headers }));
  }



  async postData(path: string, body: any): Promise<any> {
    const url = `${this.baseUrl}/${path}`;
    return firstValueFrom(this.http.post(url, body));
  }

  async putData(path: string, body: any): Promise<any> {
    const url = `${this.baseUrl}/${path}`;
    return firstValueFrom(this.http.put(url, body));
  }

  async deleteData(path: string): Promise<any> {
    const url = `${this.baseUrl}/${path}`;
    return firstValueFrom(this.http.delete(url));
  }
}
