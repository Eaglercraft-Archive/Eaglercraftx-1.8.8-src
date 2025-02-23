/*
 * Copyright (c) 2025 lax1dude. All Rights Reserved.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */

// Assuming modern GPUs probably implement clamp, max, and ciel without branches

// value1 > value2 ? 1.0 : 0.0
#define COMPARE_GT_0_1(value1, value2) clamp(ceil(value1 - value2), 0.0, 1.0)

// value1 > value2 ? N : 0.0
#define COMPARE_GT_0_ANY(value1, value2) max(ceil(value1 - value2), 0.0)

// value1 < value2 ? 1.0 : 0.0
#define COMPARE_LT_0_1(value1, value2) clamp(ceil(value2 - value1), 0.0, 1.0)

// value1 < value2 ? N : 0.0
#define COMPARE_LT_0_ANY(value1, value2) max(ceil(value2 - value1), 0.0)

// value1 > value2 ? ifGT : ifLT
#define COMPARE_GT_C_C(value1, value2, ifGT, ifLT) (COMPARE_GT_0_1(value1, value2) * (ifGT - ifLT) + ifLT)

// value1 < value2 ? ifLT : ifGT
#define COMPARE_LT_C_C(value1, value2, ifLT, ifGT) (COMPARE_LT_0_1(value1, value2) * (ifLT - ifGT) + ifGT)
