import { DayRegisterModel } from './day-register.model';

export interface WeekRegisterModel {
  totalWork: number;
  registersDay: DayRegisterModel[];
}
