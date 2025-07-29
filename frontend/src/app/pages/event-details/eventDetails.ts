import { Component, inject } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { EventService } from '../../service/event.service';
import { IEvent } from '../../models/event.model';
import {NavbarComponent} from '../../components/navbar/navbar.component';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';


@Component({
  selector: 'app-event-details',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    NavbarComponent
  ],
  templateUrl: './eventDetails.html',
})
export class EventDetails {
  private route = inject(ActivatedRoute);
  private eventApi = inject(EventService);
  private sanitizer = inject(DomSanitizer);

  event$ = this.eventApi.getEventById(this.route.snapshot.paramMap.get('id')!);

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
}
