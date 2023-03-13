const RadioGroup = ({ className, label, options, onChange, value }) => {
  return (
    <fieldset className={className}>
      <legend className="font-semibold uppercase text-gray-200">{label}</legend>
      {options.map((option) => (
        <RadioInput
          key={option.value}
          value={option.value}
          name={label}
          onChange={onChange}
          checked={value === option.value}
        >
          {option.name}
        </RadioInput>
      ))}
    </fieldset>
  );
};

const RadioInput = ({ value, name, children, onChange, checked }) => {
  const id = `radio-input-${value}`;

  return (
    <div className="my-4 flex select-none items-center">
      <input
        id={id}
        type="radio"
        name={name}
        value={value}
        className="cursor-pointer accent-sky-400"
        onChange={onChange}
        checked={checked}
      />
      <label
        htmlFor={id}
        className="ml-2 cursor-pointer text-sm font-medium text-gray-300"
      >
        {children}
      </label>
    </div>
  );
};

export default RadioGroup;
