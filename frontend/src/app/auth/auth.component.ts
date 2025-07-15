import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import {FormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService } from '../api.service';
import { AuthService } from '../auth.service';
import { User } from '../models/user.model';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './auth.component.html',
  styleUrl: './auth.component.css'
})
export class AuthComponent implements OnInit {
  email = '';
  password = '';
  error = '';
  success = '';

  constructor(
    private api: ApiService,
    private auth: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.router.navigate(['/dashboard']); // Change la route si besoin
    }
  }

  async onLogin() {
    try {
      const user: User = { email: this.email, password: this.password };
      const result: any = await this.api.login(user);
      if (result.token) {
        this.auth.setToken(result.token);
        this.error = '';
        this.success = "Authentification reussie";
        this.router.navigate(['/dashboard']); // Redirection après login
      } else {
        this.error = 'Authentification échouée';
      }
    } catch (e) {
      this.error = 'Erreur lors de la connexion';
    }
  }
}
