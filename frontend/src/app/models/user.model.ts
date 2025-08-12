export interface IUser {
  id?: string;
  email: string;
  username: string;
  firstname: string;
  lastname: string;
  role: string;
}

export interface ILogin {
  email: string;
  password: string;
}

export interface IRegister {
  email: string;
  username: string;
  firstname: string;
  lastname: string;
  password: string;
}

export interface IPasswordData {
  oldPassword: string;
  newPassword: string;
  confirmPassword: string;
}

export interface IEmailData {
  email: string;
  password: string;
}

export interface IChangeRoleData {
  userId: string;
  role: string;
}

export interface IUserPage {
  content: IUser[];
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
