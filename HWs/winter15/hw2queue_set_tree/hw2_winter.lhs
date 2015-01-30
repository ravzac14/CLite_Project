Zack Raver; ravzac14
due wk 5

1) Count the sequence of Cons operations done with the queue implementation for the following sequence of enqueueus and dequeues:

    e(4), e(5), e(7), deq, deq, e(8), deq, deq, e(9), e(10), deq, e(11)

each enqueue has an amortized cost of 2 cons, and then deq costs 0...So that sequence amortizationally costs: 2 + 2 + 2 + 0 + 0 + 2 + 0 + 0 + 2 + 2 + 0 + 2 = 14 cons
...it actually costs: 1 + 1 + 1 + 4 + 1 + 1 + 1 + 4 + 1 = 15 cons (where the expensive operations are the dequeues when the front list is empty


2) For the naive Set, with an unbalanced binary tree, determine the max length path for the set which contains the following, inserted in order:

    1,2,3,4,5,6,7,8,9,10

the path length is 9, 1-2-3-4-5-6-7-8-9-10

3) For a red-black implementation, there would be balancing as you add the nodes:

1 would be added as black, 2 would be added as red
3 would be added as red, it would rebalance, because 2 > 2(0), making 2 the root as black, and 1 left child (B) and 3 the right child (b)
4 would be added as red to the right child of (3) as red
5 would be added as red, and it would rebalance...making (4) the red right child of root (2), and 3 and 5 its children (as black)
6 would be added as red 
7 would be added as red, and it would rebalance...making (6) the b right child of 4, with 5 and 7 its black children
8 would be added as red
9 would be added as red, and it would rebalance...making 8 the red right child of 6, with 7 and 9 its black children
10 would be added as red 

4) Qualified types are generic types with constraints, (such as must be; a number, ordered, etc). We need them to control the behavior of the programmer..
Ensuring that type errors don't happen as often (like Str / Str..is not possible). Or the binary search trees must be ordered. You can't put functions in a set..because 
they're not normally ordered. 

You would add instance declarations of Eq and Ord, or derived (Eq,Ord,Show). You would order the set if you didn't want these qualification

5) An alternative Set implementation using a characterstic function.

>   type Set a = a -> Bool

>   number :: Set Integer
>   number x = (x `mod` 1) == 0

6) reverse . reverse . reverse [1,2,3,4,5]

the first reverse would take forever, so here's the more efficient one

reverse . reverse . reverse [1,2,3,4,5]
                  ~ rev [] [1,2,3,4,5]
                  ~ rev [1] [2,3,4,5]
                  ~ rev [2,1] [3,4,5]
                  ~ rev [3,2,1] [4,5]
                  ~ rev [4,3,2,1] [5]
                  ~ rev [5,4,3,2,1] []
reverse . reverse [5,4,3,2,1]
        ~ rev [] [5,4,3,2,1]
        ~ rev [5] [4,3,2,1]
        ~ rev [4,5] [3,2,1]
        ~ rev [3,4,5] [2,1]
        ~ rev [2,3,4,5] [1]
        ~ rev [1,2,3,4,5] []
reverse [1,2,3,4,5]
rev [] [1,2,3,4,5]
rev [1] [2,3,4,5]
rev [2,1] [3,4,5]
rev [3,2,1] [4,5]
rev [4,3,2,1] [5]
rev [5,4,3,2,1] []
[5,4,3,2,1]

7) These each handle the tree by it's constructor cases..the foldtree is possible because they're recursively defined
