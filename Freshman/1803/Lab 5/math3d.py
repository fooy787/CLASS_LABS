import math


# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
# @ VECTORN class                    @
# @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
class VectorN(object):
    """ This is a class which will be used to represent a vector (or a point
        in n-dimensional space. It is basically acts as a python list (of
        floats), but with extra vector operations that python lists don't
        have. """


    def __init__(self, p):
        """ The constructor.  p can be one of these type of objects:
            an integer: the dimension this vector exist in (in which case,
                 this vector is initialized to a p-dimensional zero vector)
            a sequence-like object: the values of the vector (we infer
                 the dimension based on the length of the sequence.  The values
                 are copied to this VectorN (and converted to floats)) """
        if isinstance(p, int):
            self.mDim = p
            self.mData = [0.0] * p
        elif hasattr(p, "__len__") and hasattr(p, "__getitem__"):
            # Note: We're using len(p) and p[i], so we need the above
            #   two methods.
            self.mDim = len(p)
            self.mData = []
            for i in range(len(p)):
                self.mData.append(float(p[i]))
        else:
            raise TypeError("Invalid parameter.  You must pass a sequence or an integer")


    def __str__(self):
        """ Returns a string representation of this VectorN (self) """
        s = "<Vector" + str(self.mDim) + ": "
        s += str(self.mData)[1:-1]
        s += ">"
        return s


    def __len__(self):
        """ Returns the dimension of this VectorN when a VectorN is passed to
           the len function (which is built into python) """
        return self.mDim


    def __getitem__(self, index):
        """ Returns an element of mData """
        return self.mData[index]


    def __setitem__(self, index, value):
        """ Sets the value of self.mData[index] to value """
        self.mData[index] = float(value)    # Could fail with an invalid index
                                            # error, but we'll let python handle
                                            # it.


    def __add__(self, rhs):
        """ Returns the vector sum of self and rhs (which must be a VectorN) """
        if not isinstance(rhs, VectorN) or self.mDim != rhs.mDim:
            raise TypeError("You can only add another Vector" + str(self.mDim) \
                            + " to this Vector" + str(self.mDim))
        c = self.copy()
        for i in range(rhs.mDim):
            c[i] += rhs[i]
        return c


    def __sub__(self, rhs):
        """ Returns the vector subtraction of self - rhs (which must be a VectorN) """
        if not isinstance(rhs, VectorN) or self.mDim != rhs.mDim:
            raise TypeError("You can only subtract another Vector" + str(self.mDim) \
                            + " to this Vector" + str(self.mDim))
        c = self.copy()
        for i in range(rhs.mDim):
            c[i] -= rhs[i]
        return c

    def __eq__(self, rhs):
        """ Returns True if this VectorN (self) is of the same type (VectorN)
            and dimension of rhs and all the values in self are the same as the
            values in rhs """
        if isinstance(rhs, VectorN) and self.mDim == rhs.mDim:
            for i in range(self.mDim):
                if self.mData[i] != rhs.mData[i]:
                    return False
            return True
        return False

    def __neg__(self):
        """ Returns the vector negation of self.  This is triggered by a line
            like z = -v in python """
        c = self.copy()
        for i in range(c.mDim):
            c[i] = -c[i]
        return c

    def __mul__(self, rhs):
        """ Python passes the right-hand-side of the * operator to this method.
            It needs to be a scalar (int or float).  The method returns the
            vector product """
        if not isinstance(rhs, int) and not isinstance(rhs, float):
            raise TypeError("You can only multiply a vector by a scalar")
        new_data = []
        for val in self.mData:
            new_data.append(val * rhs)
        return VectorN(new_data)


    def __rmul__(self, lhs):
        """ Python calls this if __mul__ fails (usually when a non-VectorN is on
            the left-hand-side of the * operator.  Since vector-scalar multiplication
            is commutative, just return the result of self * lhs which will
            call our __mul__ method. """
        return self * lhs

    def __truediv__(self, divisor):
        """ Returns the result of self / divisor.  This is the same as
            s * (1 / divisor), so re-use our __mul__ operator to implement this """
        if not isinstance(divisor, int) and not isinstance(divisor, float):
            raise TypeError("You can only divide a vector by a scalar.")
        return self * (1.0 / divisor)

    def __rtruediv__(self, numerator):
        """ This *would* divide something by a VectorN, which we want to
            dis-allow, so we'll raise an exception here. """
        raise NotImplementedError("You cannot divide anything by a VectorN")

    def copy(self):
        """ Returns an identical (but separate) VectorN """
        # Note: This works because our VectorN has a __len__ and __getitem__
        #     method (which is what the constructor expects)
        return VectorN(self.mData)


    def iTuple(self):
        """ Returns a tuple with the values of this vector, converted to integers """
        L = []
        for val in self.mData:
            L.append(int(val))
        return tuple(L)     # Converts the *list* L to a tuple and returns it

    def isZero(self):
        """ Returns True if this is a zero vector, False if not. """
        for val in self.mData:
            if val != 0.0:
                return False
        return True

    def magnitude(self):
        """ Returns the scalar magnitude (length) of this vector """
        mag = 0.0
        for val in self.mData:
            mag += val * val
        return mag ** 0.5


    def normalized_copy(self):
        """ Returns a normalized copy of this (non-zero) vector """
        if self.isZero():
            return ZeroDivisionError("Can't normalize a zero-vector")
        m = self.magnitude()
        new_vals = []
        for val in self.mData:
            new_vals.append(val / m)
        return VectorN(new_vals)

    def dot(self, rhs):
        """ Returns the vector sum of self and rhs (which must be a VectorN) """
        if not isinstance(rhs, VectorN) or self.mDim != rhs.mDim:
            raise TypeError("You can only add another Vector" + str(self.mDim) \
                            + " to this Vector" + str(self.mDim))
        c = self.copy()
        for i in range(rhs.mDim):
            c[i] *= rhs[i]
        return c
