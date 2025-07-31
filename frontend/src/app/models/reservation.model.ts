import {IEvent} from './event.model';
import {IUser} from './user.model';

export interface IReservation {
  id?: string;
  firstname: string;
  lastname: string;
  event: IEvent;
  user: IUser;
  reservationDate: string;
  ticketUrl?: string;
}

export interface IReservationPage {
  content: IReservation[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  totalPages: number;
  totalElements: number;
  last: boolean;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  numberOfElements: number;
  first: boolean;
  empty: boolean;
}
