import { RegisterModel } from './register.model';

export interface DayRegisterModel {
  totalWork: number;
  date: string;
  registers: RegisterModel[];
}
