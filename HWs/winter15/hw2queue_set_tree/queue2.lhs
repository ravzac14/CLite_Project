

More efficient Queue

>	data Queue a = Queue [a] [a]
>		deriving Show
>	
>	enqueue :: a -> Queue a -> Queue a
>	enqueue e (Queue front back) = Queue front (e:back)

>	dequeue :: Queue a -> (a, Queue a)
>	dequeue (Queue (e:front) back) = (e, (Queue front back))
>	dequeue (Queue [] back) = dequeue (Queue (reverse back) [])

Set implemented as a binary tree

>	data Set a = E | T (Set a) a (Set a)
>		deriving (Show, Ord, Eq)

>	empty = E

>	member x E = False
>	member x (T a y b)
>		| x < y		= member x a
>		| x > y		= member x b
>		| otherwise	= True

>	insert x E = T E x E
>	insert x t@(T a y b)
>		| x < y		= T (insert x a) y b
>		| x > y		= T a y (insert x b)
>		| otherwise	= t

Red/Black Tree as an example of a balanced bst

>	data Color = R | B
>		deriving Show

>	data RedBlackSet a = RB_E | RBT Color (RedBlackSet a) a (RedBlackSet a)
>		deriving Show

The empty tree and membership is mostly the same as before (we just
ignore the color, which is not relevant to the search.)

>	rb_empty = RB_E

>	rbmember x RB_E = False
>	rbmember x (RBT _ a y b)
>		| x < y	=	 	rbmember x a
>		| x > y =	 	rbmember x b
>		| otherwise	=	True


There are four kinds of unbalance that can occur.
We may have a black-red-red path in any of 4 ways: black
grandparent, one of two red children, one of 4 red grandchildren.
The patterns that may occur (thinking of directions from the grandparent)
are L,L, L,R, R,L, R,R. So we can capture these unbalances as patterns.

LL: change root to Red, and children to black

>	balance (RBT B (RBT R (RBT R a x b) y c) z d) =
>		RBT R (RBT B a x b) y (RBT B c z d)

LR: change root to Red and children to black

>	balance (RBT B (RBT R a x (RBT R b y c)) z d) =
>		RBT R (RBT B a x b) y (RBT B c z d)

RL: change root to red and children to black

>	balance (RBT B a x (RBT R (RBT R b y c) z d)) =
>		RBT R (RBT B a x b) y (RBT B c z d)

RR: change root to read and children to black

>	balance (RBT B a x (RBT R b y (RBT R c z d))) =
>		RBT R (RBT B a x b) y (RBT B c z d)

>	balance t = t

Then, since the root of the tree must be black, if we are
rebalancing around a root, we force its color to be black no
matter what ins returned.


SO final version:

>	rbinsert x t = makeRootBlack (ins t)
>	  where
>		ins RB_E = RBT R RB_E x RB_E
>		ins s@(RBT color a y b)
>		  | x < y			= balance (RBT color (ins a) y b)
>		  | x > y			= balance (RBT color a y (ins b))
>		  | otherwise		= s
>		makeRootBlack (RBT _ a y b) = RBT B a y b

