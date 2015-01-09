/* @author: Zack Raver; ravzac14
 * @date: 1/6/15, due week 2
 *
 * DONEEEE
 * Implement a stack and a queue and put it to the palindrome test:
 *      -Push/enqueue all the letters of a word onto the stack/queue (at the same time)
 *      -Pop/dequeue each letter, while there are letters on the stack/queue, checking to see if the new words are the same (ie a palindrom)
 */

//import java.util.ArrayList;

public class Stack_Queue{

    public static void main(String args[]){
        
        /* Making test words "racecar", "meow", and ""  */
        char[] pal = "racecar".toCharArray();
        char[] npal = "meow".toCharArray();
        char[] empty = "".toCharArray();
        
        /* Outputting test results to stdout    */
        System.out.println("Is "+new String(pal)+" a palindrome: "+palindromeTest(pal)+"\n");
        System.out.println("Is "+new String(npal)+" a palindrome: "+palindromeTest(npal)+"\n");
        System.out.println("Is "+new String(empty)+" a palindrome: "+palindromeTest(empty)+"\n");

        /* Handling and command line parameters passed in (to test) */
        for (int j = 0; j < args.length; j++){
            char[] word = args[j].toCharArray();
            System.out.println("Is "+new String(word)+" a palindrome: "+palindromeTest(word)+"\n");
        }

    }

    /* Stack_Queue's palindromeTest method:
     *      Makes a MyStack and MyQueue, runs a char[] through each and pops the back 
     *      and checks if the results are equal, and therefore a palindrome
     * @param: char[] word  - to test
     * @return: boolean - true if it is a palindrom  */
    public static boolean palindromeTest(char[] word){
        
        MyStack s = new MyStack();
        MyQueue q = new MyQueue();

        for (int i = 0; i < word.length; i++){
            s.push(word[i]);
            q.enqueue(word[i]);
        }
        s.print();
        String stackWord = "";
        char sc = '\0';
        while (!(s.isEmpty())) {
            sc = s.top();
            s.pop();
            stackWord += sc;
        }
        q.print();
        String queueWord = "";
        char qc = '\0';
        while (!(q.isEmpty())){
            qc = q.dequeue();
            queueWord += qc;
        }

        return (stackWord.equals(queueWord));
    }
}

class MyStack{
    
    /* MyStack's fields:
     * @var: int CAPACITY   - the stack's capacity before you throw an error
     * @var: char[] stack   - the actual storage unit for the data
     * @var: int top        - tracks the top of the stack
     */
    private final int CAPACITY = 50;
    private char[] stack;
    private int top;
    
    /* MyStack's constructor:
     *      Always initializes a new char[50], and the top to 0
     */
    public MyStack(){
        stack = new char[CAPACITY];
        top = 0;
    }

    /* MyStack's print method:
     *      Graphically shows our stack */
    public void print(){
        System.out.println("**************************** "+new String(stack)+" **********************************");
        System.out.println("MyStack:");
        for (int i = 0; i < top; i++){
            System.out.println("< " + stack[i] + " >");
        }
        System.out.println();
    }
    
    /* MyStack's push method:
     *      checks for the stack bounds and then pushes something onto the stack    */
    public void push(char newChar){
        try {
            if ((top + 1) >= CAPACITY){
                throw new OverflowException("Cannot push: " + newChar + ", stack limit reached!");
            } else {
                stack[top] = newChar;
                top++;
            }
        } catch (OverflowException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /* MyStack's pop method:
     *      checks for the base of the stack and then pops something from the stack */
    public void pop(){
        char togo = top();
        if (togo != '\0'){
            stack[top - 1] = '\0';  //This is the empty char (which is what stack is populated with before stuff gets put in their)
        }
        top--;
    }

    /* MyStack's top method:
     *      checks for the base of the stack and returns the top element    */
    public char top(){
        if (top > 0){
            char topo = stack[top - 1];    
            return topo;
        }
        return '\0';
    }

    public boolean isEmpty(){
        return (top() == '\0');
    }
}

class OverflowException extends Exception{
    
    private String message;

    OverflowException(String mes){
        this.message = mes;
    }

    public String getMessage(){ return this.message; }
}

class MyQueue{
    
    /* MyQueue's fields:
     * @var: int CAPACITY   - the queue's capacity before you throw an error
     * @var: char[] queue   - the actual storage unit for the data
     * @var: int end        - tracks the end of the queue
     * @var: int front      - tracks the front of the queue
     */
    private final int CAPACITY = 50;
    private char[] queue;
    private int end;
    private int front;

    /* MyQueue's constructor:
     *      Always initializes a new char[50], and the end and the front to 0
     */
    public MyQueue(){
        queue = new char[CAPACITY];
        end = 0;
        front = 0;
    }

    /* MyQueue's print method:
     *      Graphically shows our queue */
    public void print(){
        System.out.println("MyQueue:");
        if (front < end){
            System.out.println("<");
            for (int i = front; i < end; i++){
                System.out.print(queue[i] + ", ");
            }
            System.out.println("\n>");
        }
    }
    
    public boolean isEmpty(){
        return (front == end);
    }

    /* MyQueue's Enqueue method:
     *      checks for the queue's bounds and then enqueues something    
     * @param: char newChar - char to be added to the stack     */
    public void enqueue(char newChar){
        try {
            if ((end + 1) >= CAPACITY){
                throw new OverflowException("Cannot enqueue: " + newChar + ", queue limit reached!");
            } else {
                queue[end] = newChar;
                end++;
            }
        } catch (OverflowException e){
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    /* MyQueue's dequeue method:
     *      checks for the front of the queue and then dequeues something 
     * @return: char that just dequeue'd    */
    public char dequeue(){
        char togo = front();
        queue[front] = '\0';  //This is the empty char (which is what queue is populated with before stuff gets put in their)
        front++;                //moves the front of the queue
        return togo;
    }

    /* MyQueue's front method:
     *      checks for the base of the stack and returns the top element    
     * @return char, whatevers at teh front of thw queue */
    private char front(){
        if (end > front){
            return queue[front];
        }
        return '\0';
    }
}

