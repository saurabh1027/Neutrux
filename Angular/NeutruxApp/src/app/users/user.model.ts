export class User{

    constructor(
        public userId:string,
        public firstname:string,
        public lastname:string,
        public email:string,
        public roles:string[],
        private _accessToken:string,
        private _accessTokenExpirationDate:Date
    ) { }

    
    public get accessToken() : string|null {
        if (!this._accessTokenExpirationDate || new Date() > this._accessTokenExpirationDate) {
            return null;
        }
        return this._accessToken;
    }
    

}