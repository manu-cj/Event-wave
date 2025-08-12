import {Component, OnInit} from '@angular/core';
import {IEmailData, IPasswordData, IUser} from '../../models/user.model';
import {UserService} from '../../service/user.service';
import {FormsModule} from "@angular/forms";
import {LucideAngularModule, CheckCircle, XCircle} from 'lucide-angular';

@Component({
  selector: 'app-profile-data',
  imports: [LucideAngularModule, FormsModule],
  standalone: true,
  templateUrl: './profileData.component.html',
})

export class ProfileData implements OnInit {
  user: IUser = {} as IUser;
  newUserInfos: IUser = {} as IUser;
  username: string = '';
  usernameAlreadyTaken: boolean = false;
  usernameValid: boolean = false;
  userInfosSuccess: string = '';
  userInfosError: string = '';
  userInfoChange: boolean = false;
  passwordData: IPasswordData = {} as IPasswordData;

  passwordChange: boolean = false;
  passwordError: string = '';
  passwordSuccess: string = '';

  emailData: IEmailData = {} as IEmailData;
  emailAlreadyTaken: boolean = false;
  emailChange: boolean = false;
  emailChangeSuccess: string = '';
  emailChangeError: string = '';
  emailValid: boolean = false;

  protected readonly CheckCircle = CheckCircle;
  protected readonly XCircle = XCircle;

  constructor(
    private userApi: UserService,
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserInfos();
  }

  loadUserInfos() {
    this.userApi.getUserInfo().subscribe({
      next: async (result: IUser) => {
        if (result?.username && result?.email && result?.role) {
          this.user = result;
          this.username = result.username;
        }
      },
      error: () => {
        this.user = {} as IUser;
      }
    })
  }

  async usernameExist() {
    this.userApi.verifyUsername(this.newUserInfos.username).subscribe({
      next: async (result: boolean) => {
        this.usernameAlreadyTaken = result;
        if (!this.usernameAlreadyTaken) {
          this.usernameValid = true;
        }
      },
      error: () => {
        this.usernameAlreadyTaken = false;
      }
    })
  }

  async emailExist() {
    this.userApi.verifyEmail(this.emailData.email).subscribe({
      next: async (result: boolean) => {
        this.emailAlreadyTaken = result;
        if (!this.emailAlreadyTaken) {
          this.emailValid = true;
        }
      },
      error: () => {
        this.emailAlreadyTaken = false;
      }
    })
  }

  async putUserInfos() {
    try {
      const result: any = await this.userApi.putUserInfos(this.user);
      console.log(result.status);
      if (result.status === 'success') {
        this.userInfosSuccess = result.message;
        this.userInfosError = '';
        this.newUserInfos = {} as IUser;

        console.log(this.user)
      }
      else if (result.status === 'error') {
        this.userInfosError = result.message;
      }
    }
    catch (e) {
      this.userInfosError = 'Error while updating user infos';
    }
  }

  async putPassword() {
    if (this.passwordData.oldPassword === '' || this.passwordData.newPassword  === '' || this.passwordData.confirmPassword  === '') {
      this.passwordError = 'All fields are required';
      return
    }
    else if (this.passwordData.newPassword !== this.passwordData.confirmPassword) {
      this.passwordError = 'Passwords do not match';
      return
    }
    else {
      this.passwordError = '';
    }
    try {
      const result: any = await this.userApi.putPassword(this.passwordData);
      if (result.status === 'success') {
        this.passwordChange = true;
        this.passwordError = '';
        this.passwordSuccess = result.message;
        this.passwordData = {} as IPasswordData;
      }
      else if (result.status === 'error') {
        this.passwordError = result.message;
      }
    }
    catch (e) {
      this.passwordError = 'Error while updating password';
    }

  }

  async putEmail() {
    if (this.emailData.email === "" || this.emailData.password === '') {
      this.emailChangeError = 'All fields are required';
      return;
    }
    if (this.emailAlreadyTaken) {
      this.emailChangeError = 'This email is already taken';
      return;
    }
    try {
      const result: any = await this.userApi.putEmail(this.emailData);
      if (result.status === 'success') {
        this.emailChange = true;
        this.emailChangeError = '';
        this.emailChangeSuccess = result.message;
        this.emailData = {} as IEmailData;
      }
      else if (result.status === 'error') {
        this.emailChangeError = result.message;
      }
    } catch (e) {
      this.emailChangeError = 'Error while updating email';
    }
  }

}
