export class RegistrationUserModel{
    firstname:string;
    lastname:string;
    email:string;
    password:string;

    constructor(
        firstname:string,
        lastname:string,
        email:string,
        password:string
    ) {
        this.email = email
        this.firstname = firstname
        this.lastname = lastname
        this.password = password
    }

}