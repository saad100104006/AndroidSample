package uk.co.transferx.app.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uk.co.transferx.app.R;

public class SimpleCalendar extends LinearLayout {

    private TextView currentDate;
    private TextView currentMonth;
    private Button selectedDayButton;
    private Button[] days;
    LinearLayout weekOneLayout;
    LinearLayout weekTwoLayout;
    LinearLayout weekThreeLayout;
    LinearLayout weekFourLayout;
    LinearLayout weekFiveLayout;
    LinearLayout weekSixLayout;
    private LinearLayout[] weeks;

    private int currentDateDay, chosenDateDay, currentDateMonth,
            chosenDateMonth, currentDateYear, chosenDateYear,
            pickedDateDay, pickedDateMonth, pickedDateYear;
    int userMonth, userYear;
    private DayClickListener mListener;
    private Drawable userDrawable;

    private Calendar calendar;
    LinearLayout.LayoutParams defaultButtonParams;
    private LinearLayout.LayoutParams userButtonParams;

    public SimpleCalendar(Context context) {
        super(context);
        init(context);
    }

    public SimpleCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SimpleCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimpleCalendar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private String getMonthName(Date date) {
        return (String) android.text.format.DateFormat.format("MMM", date);
    }

    private void init(Context context) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();

        View view = LayoutInflater.from(context).inflate(R.layout.simple_calendar, this, true);
        calendar = Calendar.getInstance();

        weekOneLayout = view.findViewById(R.id.calendar_week_1);
        weekTwoLayout = view.findViewById(R.id.calendar_week_2);
        weekThreeLayout = view.findViewById(R.id.calendar_week_3);
        weekFourLayout = view.findViewById(R.id.calendar_week_4);
        weekFiveLayout = view.findViewById(R.id.calendar_week_5);
        weekSixLayout = view.findViewById(R.id.calendar_week_6);
        currentDate = view.findViewById(R.id.current_date);
        currentMonth = view.findViewById(R.id.current_month);

        currentDateDay = chosenDateDay = calendar.get(Calendar.DAY_OF_MONTH);
        if (userMonth != 0 && userYear != 0) {
            currentDateMonth = chosenDateMonth = userMonth;
            currentDateYear = chosenDateYear = userYear;
        } else {
            currentDateMonth = chosenDateMonth = calendar.get(Calendar.MONTH);
            currentDateYear = chosenDateYear = calendar.get(Calendar.YEAR);
        }

        currentDate.setText(String.format(Locale.getDefault(), "%d", currentDateDay));
        currentMonth.setText(getMonthName(calendar.getTime()));//ENG_MONTH_NAMES[currentDateMonth]);

        initializeDaysWeeks();
        if (userButtonParams != null) {
            defaultButtonParams = userButtonParams;
        } else {
            defaultButtonParams = getdaysLayoutParams();
        }
        addDaysinCalendar(defaultButtonParams, context, metrics);

        initCalendarWithDate(chosenDateYear, chosenDateMonth, chosenDateDay);

    }

    private void initializeDaysWeeks() {
        weeks = new LinearLayout[6];
        days = new Button[6 * 7];
        Button butts[] = new Button[6 * 7];

        weeks[0] = weekOneLayout;
        weeks[1] = weekTwoLayout;
        weeks[2] = weekThreeLayout;
        weeks[3] = weekFourLayout;
        weeks[4] = weekFiveLayout;
        weeks[5] = weekSixLayout;
    }

    private void initCalendarWithDate(int year, int month, int day) {
        if (calendar == null)
            calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        int daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String.format(Locale.getDefault(),"%d %s", 23, "may");

        chosenDateYear = year;
        chosenDateMonth = month;
        chosenDateDay = day;

        calendar.set(year, month, 1);
        int firstDayOfCurrentMonth = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d("Serge", "day of week " + firstDayOfCurrentMonth);

        calendar.set(year, month, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        int dayNumber = 1;
        int daysLeftInFirstWeek;
        int indexOfDayAfterLastDayOfMonth;

        if (firstDayOfCurrentMonth != 1) {
            daysLeftInFirstWeek = firstDayOfCurrentMonth;
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth;
            for (int i = firstDayOfCurrentMonth; i < firstDayOfCurrentMonth + daysInCurrentMonth; ++i) {
                if (currentDateMonth == chosenDateMonth
                        && currentDateYear == chosenDateYear
                        && dayNumber == currentDateDay) {
                    //          days[i].setBackgroundColor(getResources().getColor(R.color.red));
                    days[i].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_calendar_green));
                    days[i].setTextColor(Color.WHITE);
                    days[i].setEnabled(true);
                } else {
                    days[i].setTextColor(Color.BLACK);
                    days[i].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_calendar));
                    days[i].setEnabled(true);
                    //          days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = chosenDateMonth;
                dateArr[2] = chosenDateYear;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));

                days[i].setOnClickListener(this::onDayClick);
                ++dayNumber;
            }
        } else {
            daysLeftInFirstWeek = 8;
            indexOfDayAfterLastDayOfMonth = daysLeftInFirstWeek + daysInCurrentMonth;
            for (int i = 8; i < 8 + daysInCurrentMonth; ++i) {
                if (currentDateMonth == chosenDateMonth
                        && currentDateYear == chosenDateYear
                        && dayNumber == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.red));
                    days[i].setTextColor(Color.WHITE);
                } else {
                    days[i].setTextColor(Color.BLACK);
                    days[i].setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_calendar));
                    days[i].setEnabled(true);
                    //             days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                int[] dateArr = new int[3];
                dateArr[0] = dayNumber;
                dateArr[1] = chosenDateMonth;
                dateArr[2] = chosenDateYear;
                days[i].setTag(dateArr);
                days[i].setText(String.valueOf(dayNumber));

                days[i].setOnClickListener(this::onDayClick);
                ++dayNumber;
            }
        }

        if (month > 0)
            calendar.set(year, month - 1, 1);
        else
            calendar.set(year - 1, 11, 1);
        int daysInPreviousMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = daysLeftInFirstWeek - 1; i >= 0; --i) {
            int[] dateArr = new int[3];

            if (chosenDateMonth > 0) {
                if (currentDateMonth == chosenDateMonth - 1
                        && currentDateYear == chosenDateYear
                        && daysInPreviousMonth == currentDateDay) {
                } else {
                    //              days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = chosenDateMonth - 1;
                dateArr[2] = chosenDateYear;
            } else {
                if (currentDateMonth == 11
                        && currentDateYear == chosenDateYear - 1
                        && daysInPreviousMonth == currentDateDay) {
                } else {
                    //           days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = daysInPreviousMonth;
                dateArr[1] = 11;
                dateArr[2] = chosenDateYear - 1;
            }

            days[i].setTag(dateArr);
            days[i].setText(String.valueOf(daysInPreviousMonth--));
            days[i].setOnClickListener(this::onDayClick);
        }

        int nextMonthDaysCounter = 1;
        for (int i = indexOfDayAfterLastDayOfMonth; i < days.length; ++i) {
            int[] dateArr = new int[3];

            if (chosenDateMonth < 11) {
                if (currentDateMonth == chosenDateMonth + 1
                        && currentDateYear == chosenDateYear
                        && nextMonthDaysCounter == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    //             days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = nextMonthDaysCounter;
                dateArr[1] = chosenDateMonth + 1;
                dateArr[2] = chosenDateYear;
            } else {
                if (currentDateMonth == 0
                        && currentDateYear == chosenDateYear + 1
                        && nextMonthDaysCounter == currentDateDay) {
                    days[i].setBackgroundColor(getResources().getColor(R.color.red));
                } else {
                    //            days[i].setBackgroundColor(Color.TRANSPARENT);
                }

                dateArr[0] = nextMonthDaysCounter;
                dateArr[1] = 0;
                dateArr[2] = chosenDateYear + 1;
            }

            days[i].setTag(dateArr);
            days[i].setTextColor(getResources().getColor(R.color.transparent));
            days[i].setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
            days[i].setText(String.valueOf(nextMonthDaysCounter++));
            days[i].setOnClickListener(this::onDayClick);
        }

        calendar.set(chosenDateYear, chosenDateMonth, chosenDateDay);
    }

    public void onDayClick(View view) {
        if (mListener != null) {
            mListener.onDayClick(view);
        }

        if (selectedDayButton != null) {
            if (chosenDateYear == currentDateYear
                    && chosenDateMonth == currentDateMonth
                    && pickedDateDay == currentDateDay) {
                //               selectedDayButton.setBackgroundColor(getResources().getColor(R.color.red));
                selectedDayButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_calendar_green));
                selectedDayButton.setTextColor(Color.WHITE);
            } else {
               //                selectedDayButton.setBackgroundColor(Color.TRANSPARENT);
                selectedDayButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_calendar));
                if (selectedDayButton.getCurrentTextColor() != Color.RED) {
                    selectedDayButton.setTextColor(getResources()
                            .getColor(R.color.black));
                }
            }
        }

        selectedDayButton = (Button) view;
        if (selectedDayButton.getTag() != null) {
            int[] dateArray = (int[]) selectedDayButton.getTag();
            pickedDateDay = dateArray[0];
            pickedDateMonth = dateArray[1];
            pickedDateYear = dateArray[2];
        }

        if (pickedDateYear == currentDateYear
                && pickedDateMonth == currentDateMonth
                && pickedDateDay == currentDateDay) {
            selectedDayButton.setBackgroundColor(getResources().getColor(R.color.red));
            selectedDayButton.setTextColor(Color.WHITE);
        } else {
            //         selectedDayButton.setBackgroundColor(getResources().getColor(R.color.green));
            selectedDayButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_calendar_green));
            if (selectedDayButton.getCurrentTextColor() != Color.RED) {
                selectedDayButton.setTextColor(Color.WHITE);
            }
        }
    }

    private void addDaysinCalendar(LayoutParams buttonParams, Context context,
                                   DisplayMetrics metrics) {
        int engDaysArrayCounter = 0;

        for (int weekNumber = 0; weekNumber < 6; ++weekNumber) {
            for (int dayInWeek = 0; dayInWeek < 7; ++dayInWeek) {
                final Button day = new Button(context);
                day.setTextColor(ContextCompat.getColor(context, R.color.transparent));
                day.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
                day.setEnabled(false);
                //    day.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_calendar));
                day.setLayoutParams(buttonParams);
                day.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                day.setMaxLines(1);
                days[engDaysArrayCounter] = day;
                weeks[weekNumber].addView(day);
                ++engDaysArrayCounter;
            }
        }
    }

    private LayoutParams getdaysLayoutParams() {
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        buttonParams.weight = 1;
        buttonParams.setMargins(10, 10, 10, 10);
        return buttonParams;
    }

    public void setUserDaysLayoutParams(LinearLayout.LayoutParams userButtonParams) {
        this.userButtonParams = userButtonParams;
    }

    public void setUserCurrentMonthYear(int userMonth, int userYear) {
        this.userMonth = userMonth;
        this.userYear = userYear;
    }

    public void setDayBackground(Drawable userDrawable) {
        this.userDrawable = userDrawable;
    }

    public interface DayClickListener {
        void onDayClick(View view);
    }

    public void setCallBack(DayClickListener mListener) {
        this.mListener = mListener;
    }
}
