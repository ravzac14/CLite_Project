--Zack Raver
--ravzac14

data AVL e = E | N Int Int (AVL e) e (AVL e) deriving (Show)

avlEmpty :: AVL e
avlEmpty = E

node :: AVL e -> e -> AVL e -> AVL e
node l x r = N bf h l x r where
  h = 1 + $ max (height l) (height r) 
  bf = (height l) - (height r)

height :: AVL e -> Int
height E = 0
height (N _ ht _ _ _) = ht

avlLookup :: Ord e => e -> AVL e -> Bool
avlLookup x E = False 
avlLookup x (N _ _ l n r) 
  | x == n    = True
  | x < n     = avlLookup x l
  | otherwise = avlLookup x r

prop_height E = Right ()
prop_height (N bf ht l x r) = if and [((max (height l) (height r)) + 1) == ht, 
                                 (prop_height l) == Right (), 
                                 (prop_height r) == Right ()]
                              then Right ()
                              else (Left "Does not satisfy the height property")

prop_bf E = Right ()
prop_bf (N bf ht l x r) = if and [((height l) - (height r)) <= 1, 
                             prop_bf l == Right (),
                             prop_bf r == Right ()]
                          then Right ()
                          else (Left "Does not satisfy the balance factor property")

prop_balance E = Right ()
prop_balance (N bf ht l x r) = if and [(bf <= 1), (bf >= -1),
                                  prop_balance l == Right (),
                                  prop_balance r == Right ()]
                               then Right ()
                               else (Left "Some node(s) do(es) not satisfy the balance property")

prop_inorder E = Right ()
prop_inorder (N bf ht l x r) = if and [(fmap (x <) (value r)),
                                  (fmap (< x) (value l),
                                  prop_inorder l == Right (),
                                  prop_inorder r == Right ()]
                               then Right ()
                               else (Left "some nodes not in order")

value :: (Ord e) => AVL e -> Either e
value E = Left ()
value (N bf ht l x r) = Right x

check :: AVL e -> Bool
check E = True
check Ntree = and [prop_height Ntree == Right (),
                   prop_bf Ntree == Right (),
                   prop_balance Ntree == Right (),
                   prop_inorder Ntree == Right ()]

balance Ntree = if not (check Ntree) then rebalance Ntree else Ntree

rebalance tr@(N bf ht trl@(N bfl htl trll@(N bfl' htl' ll' xl' rl') xl trlr@(N bfr' htr' lr' xr' rr')) x trr@(N bfr htr trrl@(N bfl'' htl'' ll'' xl'' rl'') xr trrr@(N bfr'' htr'' lr'' xr'' rr'')))
  |  = r_rot tree
  |  = l_rot tree
  |  = rl_rot tree
  |  = lr_rot tree
  | otherwise = tree



