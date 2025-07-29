import {Component, Input} from '@angular/core';
import {IEvent} from '../../models/event.model';
import {DatePipe, NgOptimizedImage} from '@angular/common';


@Component({
  selector: 'app-events-card',
  imports: [
    DatePipe,
    NgOptimizedImage
  ],
  templateUrl: './eventCard.component.html',
})
export class EventCard {
  @Input() event!: IEvent;





}
