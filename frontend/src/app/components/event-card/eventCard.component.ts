import {Component, Input} from '@angular/core';
import {IEvent} from '../../models/event.model';
import {DatePipe} from '@angular/common';
import {environment} from '../../../environment/environment';


@Component({
  selector: 'app-events-card',
  imports: [
    DatePipe,
  ],
  templateUrl: './eventCard.component.html',
})
export class EventCard {
  @Input() event!: IEvent;
  apiUrl: string = environment.apiUrl;
  baseUrl: string = environment.baseUrl;
  fileUrl: string = environment.fileUrl;





}
