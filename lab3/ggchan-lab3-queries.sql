-- πC(W) × πA(S) × πB(T)
SELECT W.C, S.A, T.B FROM fall2017.W, fall2017.S, fall2017.T;
-- σE>F (T)
SELECT * FROM fall2017.T WHERE E > F;
-- σA6=B(R)
SELECT * FROM fall2017.R WHERE A != B;
-- πB,F (σF ≥E(T))
SELECT T.B, T.F FROM fall2017.T WHERE T.F >= T.E;
-- σA=D OR B=D(πA,B(R) × W)
SELECT R.A, R.B, W.* FROM fall2017.R, fall2017.W WHERE R.A = W.D OR R.B = W.D;
-- σA=b AND C>1(R) ∪ σB=b OR C6=3(S)
SELECT * FROM fall2017.R WHERE R.A = "b" AND R.C > 1 UNION SELECT * FROM fall2017.S where S.B = "b" or S.C != 3;
-- σNOT(B=d)(T)
SELECT * FROM fall2017.T WHERE NOT B = "d";
-- πA,B,R.C,D(σR.C=W.C(R × W))
SELECT R.A, R.B, R.C, W.C, W.D FROM fall2017.R CROSS JOIN fall2017.W WHERE R.C = W.C;
--  W ./ R
SELECT * FROM fall2017.W INNER JOIN fall2017.R;
-- W ./R.C=W.C R
SELECT * FROM fall2017.W INNER JOIN fall2017.R ON R.C = W.C;
-- T ./F >C W
SELECT * FROM fall2017.T inner join fall2017.W ON T.F > W.C;
-- R ./ S
SELECT * FROM fall2017.R INNER JOIN fall2017.S;
-- R ./R.B=S.A S
SELECT * FROM fall2017.R INNER JOIN fall2017.S ON R.B = S.A;

