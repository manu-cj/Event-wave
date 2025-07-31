import {Component, Input} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {IReservation} from '../../models/reservation.model';
import {IEvent} from '../../models/event.model';
import {IUser} from '../../models/user.model';
import {Router} from '@angular/router';
import {UserService} from '../../service/user.service';
import {ReservationService} from '../../service/reservation.service';

@Component({
  selector: 'app-reservation-form',
  imports: [FormsModule],
  templateUrl: './reservationForm.component.html',
})
export class ReservationForm {
  reservation: IReservation = {} as IReservation;
  error: string = '';
  success: string = '';
  token: string = '';
  user: IUser = {} as IUser;
  @Input() event!: IEvent;

  constructor(
    private router: Router,
    private api: UserService,
    private reservationApi: ReservationService,

  ) { }


  async onSubmit() {
    const token = localStorage.getItem('token');
    if (!token) {
      await this.router.navigate(['/login']);
      return;
    }
    this.token = token;
    this.api.getUserInfo(token).subscribe({
      next: async (result: IUser) => {
        if (result?.id && result?.username && result?.email && result?.role) {
          this.reservation.user = result;
          this.user = result;

          this.reservation.event = {
            id: this.event.id,
            title: this.event.title
          } as IEvent;
          this.reservation.reservationDate = new Date().toISOString();

          const resultApi: any = await this.reservationApi.postReservation(this.reservation);
          if (resultApi.status === 'success') {
            this.success = resultApi.message;
            this.error = '';
            this.reservation = {} as IReservation;
          } else if (resultApi.status === 'error') {
            this.error = resultApi.message;
          }
        } else {
          this.error = 'Error while getting user info';
        }
      },
      error: async () : Promise<void> => {
        this.error = 'Error while getting user info';
      }
    });
  }
}
