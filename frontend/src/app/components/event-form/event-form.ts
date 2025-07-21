import {Component, Input} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {IEvent, IEventPage} from '../../models/event.model';
import {EventService} from '../../service/event.service';

@Component({
  selector: 'app-event-form',
  templateUrl: './event-form.html',
  imports: [FormsModule]
})
export class EventFormComponent {
  @Input() token!: string;
  event: IEvent = {} as IEvent;
  selectedImage?: File;
  success = '';
  error = '';

  constructor(
    private eventApi: EventService,
  ) {}

  async onSubmit() {
    // Traitement du formulaire (ex : appel API)
    if (this.selectedImage) {
      const result : any = await this.eventApi.postEvent(this.event, this.token, this.selectedImage);
      
    } else {
      console.log('Aucune image sélectionnée');
    }
  }


  onImageSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.selectedImage = file;
      console.log(file);
    }
  }
}
