export class UserModel{
    userId:string;
    firstname:string;
    lastname:string;
    email:string;

    constructor(
        userId:string,
        firstname:string,
        lastname:string,
        email:string
    ) {
        this.userId = userId
        this.email = email
        this.firstname = firstname
        this.lastname = lastname
    }

}