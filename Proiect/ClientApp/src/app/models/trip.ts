import { IUser } from "./user";

export interface Trip {
    id: number;
    group_size: number;
    location: string;
    trip_name: string;
    admin: IUser;
}