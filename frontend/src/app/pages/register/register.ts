import { Component, OnInit } from '@angular/core';
import {FormsModule} from '@angular/forms';
import { CommonModule } from '@angular/common';
import {ApiService} from '../../service/api.service';
import {UserService} from '../../service/user.service';
import {AuthService} from '../../service/auth.service';
import { Router } from '@angular/router';
import {IRegister} from '../../models/user.model';
import {NavbarComponent} from '../../components/navbar/navbar.component';



@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule, NavbarComponent],
  templateUrl: './register.html',
})
export class Register implements OnInit{
  user : IRegister = {} as IRegister;
  passwordRepeat = '';
  error = '';
  success = '';



  constructor(
    private api: UserService,
    private router: Router,
    private auth: AuthService,
  ) {}


  ngOnInit(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.api.getUserInfo(token).subscribe({
        next: () => {
          this.router.navigate(['/dashboard']);
        },
        error: () => {
          localStorage.removeItem('token');
          this.error = 'Session expirée ou token invalide. Veuillez vous reconnecter.';
        }
      });
    }
  }

  async onRegister() {
    if (this.user === undefined || this.user.password !== this.passwordRepeat) {
      this.error = "Les mots de passe ne correspondent pas";
      return;
    }
    try {

      /* TODO CHANGE IN postUser AFTER */
      const result: any = await this.api.postUser(this.user);
      console.log(result);
      if (result.status === 'success') {
        this.success = result.message;
        this.error = '';
        await this.router.navigate(['/login']);
      }
      else if (result.status === 'error') {
        this.error = result.message;
      }
    }
    catch (e) {
      this.error = "Error when registering :";
    }
  }
}
