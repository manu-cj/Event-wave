import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {firstValueFrom, Observable} from 'rxjs';
import {ILogin, IRegister, IUser} from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = 'http://localhost:8081/api';

  constructor(private http: HttpClient) {}

  getUserInfo(token: string): Observable<IUser> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>(this.baseUrl + '/auth/me', { headers });
  }

  async postUser(user: IRegister): Promise<IRegister> {
    return firstValueFrom(this.http.post<IRegister>(`${this.baseUrl}/auth/register`, user));
  }

  async login(user: ILogin): Promise<ILogin> {
    return firstValueFrom(this.http.post<ILogin>(`${this.baseUrl}/auth/login`, user));
  }

  async postAdmin(user: IRegister): Promise<IRegister> {
    return firstValueFrom(this.http.post<IRegister>(`${this.baseUrl}/auth/register/admin`, user));
  }


}
