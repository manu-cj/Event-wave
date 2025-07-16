export interface IUser {
  id?: number;
  email: string;
  username: string;
  firstname: string;
  lastname: string;
  password: string;
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
