export class BlogElementModel{

    constructor (
        public elementId:string,
        public name:string,
        public description:string,
        public value:string,
        public position:number,
        public blogId:string
    ) {}

}

/*

options: 
    size = full|medium|half
    position = left|medium|right

*/