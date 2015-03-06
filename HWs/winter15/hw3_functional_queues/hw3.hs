--Zack Raver
--ravzac14

data Q a = T [a] [a] [a]
    deriving (Show)

len (T l r l')  = length l + length r

insert e (T l r l')  = makeq (T l (e:r) l')

remove (T (l:ls) r l') = (l, (makeq (T ls r l')))

makeq (T l r []) = T l'' [] l''
    where l''  = rot l r []
makeq (T l r (_:l')) =  T l r l'

rot [] (r:rs) a = r:a
rot (l:ls) (r:rs) a = l:(rot ls rs (r:a))

--append
append (T l r l') (T m n m')  = makeeq (T l (r ++ m ++ (reverse n)) l')

--fold


--map

--reverse

--rewrite append

--analyze Big O

--implement deque as described in the paper
