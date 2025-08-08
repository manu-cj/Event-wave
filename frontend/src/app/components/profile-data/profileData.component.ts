import {Component, OnInit} from '@angular/core';
import {IUser} from '../../models/user.model';
import {UserService} from '../../service/user.service';
import {FormsModule} from "@angular/forms";
import {LucideAngularModule, CheckCircle, CircleX, XCircle} from 'lucide-angular';

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
  emailAlreadyTaken: boolean = false;
  userInfosSuccess: string = '';
  userInfosError: string = '';
  userInfoChange: boolean = false;

  readonly CheckCircle = CheckCircle;
  readonly CircleX = CircleX;

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

  protected readonly XCircle = XCircle;
}
