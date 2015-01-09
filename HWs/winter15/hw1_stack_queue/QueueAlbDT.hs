--Zack Raver; ravzac14
--1/9/15

--This is an algebraic implementation for Queue, exported as an ADT

module Queue(Queue(),enQ,deQ,emptyQ,isEmpty) where

data Queue a = EmptyQueue | EnQ a (Queue a) deriving (Show)

enQ :: a -> Queue a -> Queue a
enQ x q         = EnQ x q

deQ :: Queue a -> Queue a
deQ EmptyQueue  = error "Empty queue, nothing to dequeue"
deQ (EnQ x qs)  = qs

emptyQ :: Queue a
emptyQ          = EmptyQueue

isEmpty :: Queue a -> Bool
isEmpty EmptyQueue  = True
isEmpty _           = False
