import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {firstValueFrom, Observable} from 'rxjs';
import {IEmailData, ILogin, IPasswordData, IRegister, IUser, IUserPage} from '../models/user.model';
import {environment} from '../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  //get user info with token
  getUserInfo(): Observable<IUser> {

    return this.http.get<any>(this.baseUrl + '/auth/me', { withCredentials: true });
  }

  //create user
  async postUser(user: IRegister): Promise<IRegister> {
    return firstValueFrom(this.http.post<IRegister>(`${this.baseUrl}/auth/register`, user));
  }

  //login user
  async login(user: ILogin): Promise<ILogin> {
    return firstValueFrom(this.http.post<ILogin>(`${this.baseUrl}/auth/login`, user, { withCredentials: true }));
  }

  // logout user
  async logout(): Promise<any> {
    return firstValueFrom(this.http.post<any>(`${this.baseUrl}/auth/logout`, {}, { withCredentials: true }));
  }

  // create Admin
  async postAdmin(user: IRegister): Promise<IRegister> {
    return firstValueFrom(this.http.post<IRegister>(`${this.baseUrl}/auth/register/admin`, user));
  }

  // get all user with pagination
  getUsers(param: string, page: number, size: number, column: string, direction: string): Observable<IUserPage> {

    return this.http.get<IUserPage>(`${this.baseUrl}/users?param=${param}&page=${page}&size=${size}&sort=${column},${direction}`, { withCredentials: true });
  }

  // verify if a username already exists
  verifyUsername(username: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/users/exists/username/${username}`);
  }

  // verify if email already exists
  verifyEmail(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.baseUrl}/users/exists/email/${email}`);
  }

  // put userInfos
  async putUserInfos(userInfos: IUser): Promise<IUser> {
    return firstValueFrom(this.http.put<IUser>(`${this.baseUrl}/users`, userInfos, { withCredentials: true }));
  }

  // put password
  async putPassword(passwordData: IPasswordData): Promise<IUser> {
    return firstValueFrom(this.http.put<IUser>(`${this.baseUrl}/users/change-password`, passwordData, { withCredentials: true }));
  }

  // put email
  async putEmail(emailData: IEmailData): Promise<IUser> {
    return firstValueFrom(this.http.put<IUser>(`${this.baseUrl}/users/change-email`, emailData, { withCredentials: true }));
  }


}
