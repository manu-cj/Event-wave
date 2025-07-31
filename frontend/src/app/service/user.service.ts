import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {firstValueFrom, Observable} from 'rxjs';
import {ILogin, IRegister, IUser, IUserPage} from '../models/user.model';
import {environment} from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  //get user info with token
  getUserInfo(token: string): Observable<IUser> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    return this.http.get<any>(this.baseUrl + '/auth/me', { headers });
  }

  //create user
  async postUser(user: IRegister): Promise<IRegister> {
    return firstValueFrom(this.http.post<IRegister>(`${this.baseUrl}/auth/register`, user));
  }

  //login user
  async login(user: ILogin): Promise<ILogin> {
    return firstValueFrom(this.http.post<ILogin>(`${this.baseUrl}/auth/login`, user));
  }

  // create Admin
  async postAdmin(user: IRegister): Promise<IRegister> {
    return firstValueFrom(this.http.post<IRegister>(`${this.baseUrl}/auth/register/admin`, user));
  }

  // get all user with pagination
  getUsers(page: number, size: number): Observable<IUserPage> {
    return this.http.get<IUserPage>(`${this.baseUrl}/users?page=${page}&size=${size}`);
  }


}
