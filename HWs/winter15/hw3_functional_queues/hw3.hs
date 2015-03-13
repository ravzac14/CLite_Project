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
append' (T l r l') (T m n m') 
  | length (T l r l') > 0  = append ll'' (insert ll' (T m n m'))
      where (ll', ll'') = remove (T l r l')
  | otherwise   = T m n m'

--fold
fold' f (T l r l') acc 
  | length (T l r l') > 0 =  fold' f ll'' (f ll' acc) 
      where (ll', ll'') = remove (T l r l')
  | otherwise = acc


--map
map' f (T l r l') T (map f l) (map f r) (map f l')

--reverse
reverse' (T l r l') = T r l l'

--rewrite append
append (T [1,2] [4,3] []) (T [1,2] [4,3] [])
append (makeq (T [2] [4,3] []) (insert 1 (T [1,2] [4,3] []))
append (T (rot [2] [4,3] []) [] (rot (T [2] [4,3] [])))) (makeq (T [1,2] [1,4,3] []))
append (T (2:(rot [] [3] [4])) [] (2:(rot [] [3] [4]))) (T (rot [1,2] [1,4,3] []) [] (rot [1,2] [1,4,3] []))
append (T (2:3:4) [] (2:3:4)) (T (1:(rot [2] [4,3] [1])) [] (1(rot [2] [4,3] [1])))
append (T [2,3,4] [] [2,3,4]) (T (1:2:(rot [] [3] [4,1])) [] (1:2:(rot [] [3] [4,1])))
append (T [2,3,4] [] [2,3,4]) (T (1:2:3:[4,1]) [] [(1:2:3[4,1]))
append (makeq (T [3,4] [] [2,3,4]) (insert 2 (T [1,2,3,4,1] [] [1,2,3,4,1]))
append (T [3,4] [] [3,4]) (T [1,2,3,4,1] [2] [1,2,3,4,1])
append (makeq (T [4] [] [4])) (insert 3 (T [1,2,3,4,1] [2] [1,2,3,4,1]))
append (T [4] [] []) (T [1,2,3,4,1] [3,2] [1,2,3,4,1])
append (T [] [] []) (insert 4 (T [1,2,3,4,1] [3,2] [1,2,3,4,1]))
T [1,2,3,4,1] [4,3,2] [1,2,3,4,1]

--analyze Big O
Well makeq and rot make the remove, insert O(1)
And length is O(n)

so append does length check for the length of the first list of length n,,0 so append is O(nlogn) plus and insert and remove for every n  O(3n log n) so nlogn
fold compares length for length of n...0 so nlogn + remove for n 2nlogn so nlogn
map just maps down each individual thing, so n
reverse is pattern matching so O(1)

--implement deque as described in the paper

data DQ a = D [a] [a] [a] [a]
    deriving (Show)

len (D l r l'r')  = length l + length r

insertL e (D l r l' r')  = makedq (D (e:l) r (tail l') (tail r'))
insertR e (D l r l' r')  = makedq (D l (e:r) (tail l') (tail r'))

removeL (D l r l' r') 
  | (length l) > 0 = (head l, makedq (D (tail l) r (tail (tail l')) (tail (tail r'))))
  | otherwise      = (head r, D [] [] [] [])
removeR (D l r l' r') 
  | (length r) > 0 = (head l, makedq (D l (tail r) (tail (tail l')) (tail (tail r'))))
  | otherwise      = (head r, D [] [] [] [])

makedq (D l r l' r') 
  | length l > (3*(length r + 1))  =  let n = len/2
                                          l'' = take n l
                                          r'' = rot1 n r l
                                      in (D l'' r'' l' r')
  | length r > (3*(length l + 1))  =  let n = len/2
                                          l'' = rot1 n l r
                                          r'' = take n r
                                      in (D l'' r'' l' r')
  | otherwise                      =  (D l r l' r')                                     
                                        where len = (length l + length r) 

rot1 n (l:ls) (r:r':r'':rs)  
  | n >= 3     = l:(rot1 (n - 3) ls rs) 
  | otherwise  = rot2 l (drop n r) []  

rot2 (l:ls) r a
  | (length l > 0) && (length r >= 3)   = l:(rot2 ls (drop 3 r) (reverse . take 3 r) []  
  | otherwise                           = l ++ reverse r ++ a
