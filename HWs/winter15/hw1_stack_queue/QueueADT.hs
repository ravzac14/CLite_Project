--Zack Raver; ravzac14
-- 1/9/15

--a module containing an efficient ADT implementation of a Queue

module Queue (Queue(),enQ,deQ,emptyQ,isEmpty) where


-- Here we represent a queue as two list, where the head element is at the head of the 2nd list, and the tail of the 1st
--                     (sx) (xs)
data Queue a = Queue [a] [a] deriving (Show)

emptyQ :: Queue a
emptyQ                  = Queue [] []

enQ :: a -> Queue a -> Queue a
enQ x (Queue _ [])      = Queue [] [x]
enQ x (Queue sx xs)     = Queue (x:sx) xs

deQ :: Queue a -> Queue a
deQ (Queue sx (_:s@(_:_)))  = Queue sx s
deQ (Queue sx [_])          = Queue [] (reverse sx)

isEmpty :: Queue a -> Bool
isEmpty (Queue _ xs)    = null xs 
