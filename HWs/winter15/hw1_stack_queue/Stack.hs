--Zack Raver; ravzac14
--1/9/15

--module for an ADT implementation of a stack, using the algebraic data type Stack made here

module Stack (Stack(),push,top,pop,emptyStack,empty) where

data Stack a = EmptyStack | Push a (Stack a)    deriving (Show)

push :: a -> Stack a -> Stack a
push x s            = Push x s

top :: Stack a -> a
top (EmptyStack)    = error "Stack is empty"
top (Push x s)      = x

pop :: Stack a -> Stack a
pop (EmptyStack)    = error "Stack is empty"
pop (Push x s)      = s

emptyStack :: Stack a
emptyStack          = EmptyStack

empty :: Stack a -> Bool
empty (EmptyStack)  = True
empty (Push x s)    = False
