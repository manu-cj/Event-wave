import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import { firstValueFrom } from 'rxjs';

import { Product } from '../models/product.model';
import {environment} from '../../environment/environment';

@Injectable({ providedIn: 'root' })
export class ApiService {
  // Remplace l’URL par celle de ton API
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  async getData(path?: string): Promise<any> {
    const url = path ? `${this.baseUrl}/${path}` : this.baseUrl;
    return firstValueFrom(this.http.get(url));
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
