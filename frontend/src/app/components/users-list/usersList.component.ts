import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {IChangeRoleData, IUserPage} from '../../models/user.model';
import {UserService} from '../../service/user.service';
import {interval, Subject, takeUntil} from 'rxjs';
import {FormsModule} from '@angular/forms';
import {LucideAngularModule, Search, Ticket, Funnel, CircleUser, ShieldUser} from 'lucide-angular';
import {NgClass} from '@angular/common';

@Component({
  selector: 'app-users-list',
  imports: [
    FormsModule,
    LucideAngularModule,
    NgClass
  ],
  templateUrl: './usersList.component.html',
})

export class UsersList implements OnInit, OnDestroy {
  userPage: IUserPage | null = null;
  param: string = '';
  page: number = 0;
  pageSize: number = 10;
  sortedColumn: string = 'createdAt';
  sortedDirection: string = 'desc';
  selectedFilter: string = '';
  changeRoleData: IChangeRoleData = {} as IChangeRoleData;
  notification: { type: string; message: string; show: boolean } = {
    type: '',
    message: '',
    show: false
  }
  @Input() token!: string;
  private destroy$ = new Subject<void>();

  // Icons
  protected readonly Search = Search;
  protected readonly Funnel = Funnel;
  protected readonly CircleUser = CircleUser;
  protected readonly ShieldUser = ShieldUser;

  constructor(
    private api: UserService,
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUsers();
    interval(10000)
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.loadUsers());
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  loadUsers() : void {
    this.api.getUsers(this.param, this.page, this.pageSize, this.sortedColumn, this.sortedDirection).subscribe({
      next: async (result: IUserPage) => {
        this.userPage = result;
      }
    });
  }

  applySelectedFilter() : void {
    switch (this.selectedFilter) {
      case 'dateAsc':
        this.filterDateAsc();
        break;
      case 'dateDesc':
        this.filterDateDesc();
        break;
      case 'usernameAsc':
        this.filterUsernameAsc();
        break;
      case 'usernameDesc':
        this.filterUsernameDesc();
        break;
    }
  }

  filterDateAsc() : void {
    this.sortedColumn = 'createdAt';
    if (this.sortedDirection === 'desc') {
      this.sortedDirection = 'asc';
    }
    this.loadUsers();
  }

  filterDateDesc() : void {
    this.sortedColumn = 'createdAt';
    if (this.sortedDirection === 'asc') {
      this.sortedDirection = 'desc';
    }
    this.loadUsers();
  }

  filterUsernameAsc() : void {
    this.sortedColumn = 'username';
    if (this.sortedDirection === 'desc') {
      this.sortedDirection = 'asc';
    }
    this.loadUsers();
  }
  filterUsernameDesc() : void {
    this.sortedColumn = 'username';
    if (this.sortedDirection === 'asc') {
      this.sortedDirection = 'desc';
    }
    this.loadUsers();
  }

  changeRole(userId: string, role: string) : void {
    this.changeRoleData.role = role;
    this.changeRoleData.userId = userId;
    console.log(this.changeRoleData)
    this.api.changeRole(this.changeRoleData)
      .then((response: any) => {
        if (response.status === "success") {
          this.notification = {
            type: 'success',
            message: 'Role change with success.',
            show: true
          };
          setTimeout(() => {
            this.notification = {
              type: 'success',
              message: '',
              show: false
            };
          }, 5000)
          this.loadUsers();
        } else {
          this.notification = {
            type: 'error',
            message: 'Error when you change role',
            show: true
          };

          setTimeout(() => {
            this.notification = {
              type: 'error',
              message: '',
              show: false
            };
          }, 5000)
        }
      })
      .catch(() => {
        this.notification = {
          type: 'error',
          message: 'Erreur lors du changement de rôle.',
          show: true
        };
      });

  }

  nextPage() : void {
    this.page++;
    this.loadUsers();
  }

  prevPage() : void {
    if (this.page > 0) {
      this.page--;
      this.loadUsers();
    }
  }
}
