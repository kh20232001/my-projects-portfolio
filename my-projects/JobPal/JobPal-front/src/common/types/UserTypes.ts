import type { AuthorityType } from './AuthorityTypes';


export type UserType = {
  id?: number;
  userId: string;
  userName: string;
  userClass: string;
  classNumber: number;
  schoolNumber: number;
  grant: AuthorityType;
  status: string;
  createUserId: string;
  createDatetime: string;
};

export type UserTypeForView = UserType & {
  authority: string;
};
