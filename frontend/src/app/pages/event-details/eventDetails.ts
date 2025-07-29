import {Component, inject, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { EventService } from '../../service/event.service';
import {NavbarComponent} from '../../components/navbar/navbar.component';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import {UserService} from '../../service/user.service';
import {IUser} from '../../models/user.model';
import {Observable} from 'rxjs';
import {IEvent} from '../../models/event.model';
import {EventFormComponent} from '../../components/event-form/event-form';
import {Modal} from '../../components/modal/modal';


@Component({
  selector: 'app-event-details',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    NavbarComponent,
    EventFormComponent,
    Modal
  ],
  templateUrl: './eventDetails.html',
})
export class EventDetails {
  private route = inject(ActivatedRoute);
  private eventApi = inject(EventService);
  private sanitizer = inject(DomSanitizer);
  user : IUser = {} as IUser;
  isModaleOpen: boolean = false;

  event$: Observable<IEvent> = this.eventApi.getEventById(this.route.snapshot.paramMap.get('id')!);

  safeMapUrl: SafeResourceUrl = '';

  constructor() {
    this.event$.subscribe(event => {
      if (event) {
        const address = encodeURIComponent(
          `${event.address ?? ''} ${event.postalCode ?? ''} ${event.city ?? ''}`
        );

        const url = `https://www.google.com/maps?q=${address}&output=embed`;
        this.safeMapUrl = this.sanitizer.bypassSecurityTrustResourceUrl(url);
      }
    });
  }

  openModal() {
    this.isModaleOpen = true;
  }

  closeModal() {
    this.isModaleOpen = false;
  }


}
